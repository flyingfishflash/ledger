insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 0,1,'root',12, NULL);
insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 1,2,'Assets',3,1);
insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 1,4,'Liabilities',5,1);
insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 1,6,'Equity',7,1);
insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 1,8,'Income',9,1);
insert into account (discriminator, tree_level, tree_left, name, tree_right, parent_id) values ('account', 1,10,'Expense',11,1);
