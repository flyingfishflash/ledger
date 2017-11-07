insert into account (guid, account_class, account_type, discriminator, tree_level, tree_left, name, longname, tree_right, parent_id, hidden, placeholder, tax_related) values 
('5e9d933c-8411-4713-991c-91272e7b82f9', 'Root', 'Root', 'account', 0, 1, 'root', 'root', 12, NULL, 'f', 't', 'f'),
('b55b3bdb-ffa0-46d0-a3a4-575cb6228691', 'Asset', 'Asset', 'account', 1, 2, 'Assets', 'Assets', 3, 1, 'f', 't', 'f'),
('1903f813-332e-4dc7-aee5-af5c17c9e410', 'Liability', 'Liability', 'account', 1, 4, 'Liabilities', 'Liabilities', 5, 1, 'f', 't', 'f'),
('7a532c2e-c949-40de-a1d4-98806a6f4e5c', 'Income', 'Income', 'account', 1, 6, 'Income', 'Income', 7, 1, 'f', 't', 'f'),
('c2e55a0e-08d1-4f18-ac24-ac2149742293', 'Expense', 'Expense', 'account', 1, 8, 'Expense', 'Expense', 9, 1, 'f', 't', 'f'),
('3f3f5698-2050-451e-93f6-4ac0edc2641a', 'Equity', 'Equity', 'account', 1, 10, 'Equity', 'Equity', 11, 1, 'f', 't', 'f');
