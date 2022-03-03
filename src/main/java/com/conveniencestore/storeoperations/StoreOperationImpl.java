package com.conveniencestore.storeoperations;

import com.conveniencestore.enums.ProductCategory;
import com.conveniencestore.enums.Role;
import com.conveniencestore.exception.*;
import com.conveniencestore.models.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.conveniencestore.enums.ProductAvailability.*;
import static com.conveniencestore.enums.Qualification.OND;

public class StoreOperationImpl implements StoreOperation {
    @Override
    public void hireCashier(Applicant applicant, Staff staff, Store store) {
        if (staff.getRole().equals(Role.MANAGER)) {
            if (store.getApplicants().contains(applicant)) {
                if (applicant.getQualification().equals(OND)) {
                    Staff newStaff = new Staff(
                            applicant.getFirstName(),
                            applicant.getLastName(),
                            applicant.getAddress(),
                            applicant.getGender(),
                            Role.CASHIER
                    );
                    store.getStaffs().add(newStaff);
                    store.getApplicants().remove(applicant);
                    System.out.println("Congratulations, " + applicant.getFirstName() + " you have been hired!");

                } else {
                    throw new ApplicantNotQualifiedException("You must have an " + OND + " to be eligible for this position");
                }
            } else {
                throw new ApplicantNotAppliedException(applicant.getFirstName() + " did not apply for this role");
            }
        } else {
            throw new StaffNotAuthorizedException("You are not authorized to hire an applicant");
        }
    }


    @Override
    public String sellProducts(Store store, Customer customer, Staff staff) {
        if (staff.getRole().equals(Role.CASHIER)) {
            double totalCostOfProductsInCart = customer.getCart().getTotalCostOfProductsInCart();
            Map<Product, Integer> productsInCart = customer.getCart().getProductsInCart();
            for (Map.Entry<Product, Integer> products : productsInCart.entrySet()) {
                Product product = products.getKey();
                Integer quantity = products.getValue();
                totalCostOfProductsInCart += getTotalCostOfProductsInCart(product, quantity);
            }
            return checkIfCustomerHasEnoughFunds(store, customer, staff, totalCostOfProductsInCart);
        } else {
            throw new StaffNotAuthorizedException("You are not authorized to sell products");
        }

    }

    private String checkIfCustomerHasEnoughFunds(Store store, Customer customer, Staff staff, double totalCostOfProductsInCart) {
        if (customer.getWallet().getBalance() >= totalCostOfProductsInCart) {
            double newWalletBalance = customer.getWallet().getBalance() - totalCostOfProductsInCart;
            customer.getWallet().setBalance(newWalletBalance);
            String receipt = generateReceipt(store, customer, totalCostOfProductsInCart, staff);
            customer.getCart().setTotalCostOfProductsInCart(0);
            customer.getCart().getProductsInCart().clear();
            return receipt;
        } else {
            throw new InSufficientFundsException("Your balance is not enough to perform this transaction");
        }
    }

    @Override
    public Product [] viewProductsByCategory(Store store, String productCategory) {
        System.out.printf("Viewing products in %s category...\n", productCategory);
        ProductCategory category = ProductCategory.valueOf(productCategory.toUpperCase());

        List<Product> productInCategory = Arrays.stream(store.getListOfProductsInStore())
                .filter(product -> product.getProductCategory().equals(category))
                .collect(Collectors.toList());

        if (productInCategory.size() == 0) {
            System.out.println("There are no products in this category yet");
        }
        return productInCategory.toArray(new Product[0]);
    }

    private String generateReceipt(Store store, Customer customer, double totalCostOfProductsInCart, Staff staff) {
        StringBuilder productsBought = new StringBuilder();


        for (Map.Entry<Product, Integer> product : customer.getCart().getProductsInCart().entrySet()) {
            productsBought.append(product.getKey().getName())
                    .append(", ")
                    .append("Price: ")
                    .append(product.getKey().getPrice())
                    .append(",")
                    .append(" Quantity bought: ")
                    .append(product.getValue())
                    .append("\n");
        }

        return "\n" +
                "Payment Successful! \n" +
                "Receipt For : " + customer.getFirstName() + "\n" +
                "======================\n" +
                "Products bought : " + productsBought + "\n" +
                "======================\n" +
                "Total cost of Products bought : " + totalCostOfProductsInCart + "\n" +
                "======================\n" +
                "Processed by " + staff.getFirstName() + "\n" +
                "======================\n" +
                "Thank you for shopping with us at " + store.getName() + "!" + "\n";
    }

    private double getTotalCostOfProductsInCart(Product product, Integer quantity) {
        double totalCostOfProductsInCart;
        if(product.getProductAvailability().equals(AVAILABLE)) {
            if (product.getQuantity() >= quantity) {
                totalCostOfProductsInCart = product.getPrice() * quantity;
                product.setQuantity(product.getQuantity() - quantity);
                product.checkAndSetAvailability();
                return totalCostOfProductsInCart;
            } else {
                throw new QuantityExceededException("We do not have up to " + quantity
                        + " available, " + "only " + product.getQuantity() + " is/are left.");
            }
        }
        else throw new OutOfStockException("Product " + product.getName() + " is no longer available in the store");
    }
}
