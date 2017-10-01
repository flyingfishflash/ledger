### Design Thoughts


Entities:

Account  
Transaction  
Split  
Commodity  
Currency (undecided)  
Price  
Institution

Transactions are collections of Splits  
- A Transaction consists of a minimum of two splits  
- Each Split is an amount credited or debited to an Account  
- The sum of the amounts of all splits associated with a transaction should be zero  

Institutions
- Institutions may be associated with one or more Accounts


Notes:

Nested Sets  
- http://mikehillyer.com/articles/managing-hierarchical-data-in-mysql/  
- http://www.quesucede.com/page/show/id/java-tree-implementation  