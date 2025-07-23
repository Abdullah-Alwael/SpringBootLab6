# Financial Currency Exchange System Validation Checklist
### The system consists of 5 entities that interact to facilitate currency exchange and investment

### It consists of Banks, Customers, Investors, Cards, and Currencies.

**Bank model has:** ID, name, location.  
**Customer model has:** ID, name, phoneNumber, InitialCurrency, amount.   
**Investors model has** ID, name, phoneNumber, InitialCurrency, amount, investmentPercentage, returnOnInvestment.   
**Cards model has** ID, number, hasNFCPayment, cvcPassword, securityCode.  
**Currencies model has** ID, code, multiplier. 

## Checklist for validation
### **Bank:**  
- ID: String
    - @NotNull(message = "ID must not be empty")

- name: String
    - @NotNull(message = "name must not be empty")
- location: String  
    - @NotNull(message = "name must not be empty")

must add the annotations with messages

the flow of the program (BA).
deadline tomorrow 9AM

