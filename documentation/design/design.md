

### Design Thoughts


Five Essential Objects (Entities):

Accounts  
Transactions  
Splits  
Commodities  
Prices  

Transactions are collections of Splits  
- A Transaction consists of a minimum of two splits  
- Each Split is an amount credited or debited to an Account  
- The sum of the amounts of all splits associated with a transaction should be zero  

Institutions
- Accounts may, but are not required, to be associated with an institution
