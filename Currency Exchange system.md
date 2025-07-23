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
    - @NotEmpty(message = "ID must not be empty")
    - @Size(min = 12, message = "ID must not be less than 12 characters")
- name: String
    - @NotEmpty(message = "name must not be empty")
    - @Size(min = 2, message = "name must not be less than 2 characters")
- location: String  
    - @NotEmpty(message = "name must not be empty")
    - @Size(min = 15, message = "location must not be less than 15 characters")  

### **Customer:**  
- ID: String
    - @NotEmpty(message = "ID must not be empty")
    - @Size(min = 12, message = "ID must not be less than 12 characters")
- name: String
    - @NotEmpty(message = "name must not be empty")
    - @Size(min = 2, message = "name must not be less than 2 characters")
    - @Pattern(regexp = "^\\D*$", message = "name must not contain numbers")
- phoneNumber: String
    - @NotNull(message = "Phone number must not be empty")
    - @Pattern(regexp = "^05\\d{8}$", message = "phone number must start with 05 and must be 10 digits only!")
- InitialCurrency: String
    - @NotEmpty(message = "Currency must not be empty")
    - @Size(min = 3, message = "Currency must not be less than 3 characters")
    - @Pattern(regexp = "^\\D*$", message = "Currency must not contain numbers")
- amount: Double 
    - @PositiveOrZero(message = "The amount must be positive or zero")

### **Investor:**  
- ID: String
    - @NotEmpty(message = "ID must not be empty")
    - @Size(min = 12, message = "ID must not be less than 12 characters")
- name: String
    - @NotEmpty(message = "name must not be empty")
    - @Size(min = 2, message = "name must not be less than 2 characters")
    - @Pattern(regexp = "^\\D*$", message = "name must not contain numbers")
- phoneNumber: String
    - @NotNull(message = "Phone number must not be empty")
    - @Pattern(regexp = "^05\\d{8}$", message = "phone number must start with 05 and must be 10 digits only!")
- InitialCurrency: Currency
    - @NotEmpty(message = "Currency must not be empty")
    - @Size(min = 3, message = "Currency must not be less than 3 characters")
    - @Pattern(regexp = "^\\D*$", message = "Currency must not contain numbers")
- amount: Double 
    - @PositiveOrZero(message = "The amount must be positive or zero")
- investmentPercentage: Integer
    - @Positive(message = "The investment percentage must be positive")
    - @Min(value = 20, message = "The minimum investment amount is 20 percent")
    - @Max(value = 60, message = "The maximum amount of investment is 60 percent")
- returnOnInvestment: Double
    - @Pattern(regexp = "^\\d$", message = "retern on investemt must be a number")

### **Card:**  
- ID: String
    - @NotEmpty(message = "ID must not be empty")
    - @Size(min = 12, message = "ID must not be less than 12 characters")
- name: String
    - @NotEmpty(message = "name must not be empty")
    - @Size(min = 2, message = "name must not be less than 2 characters")
    - @Pattern(regexp = "^\\D*$", message = "name must not contain numbers")
- hasNFCPayment: Boolean
    - @AssertTrue(message = "NFC payment must be initially true")
- cvcPassword: String
    - @NotEmpty(message = "CVC password must not be empty")
    - @Size(min = 3, max = 3, message = "CVC password is always 3 characters")
- securityCode: String
    - @NotEmpty(message = "security code must not be empty")
    - @Size(min = 3, max = 3, message = "security code is always 3 characters")