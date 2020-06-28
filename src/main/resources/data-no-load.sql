insert into account (guid, account_class, account_type, discriminator, tree_level, tree_left, name, longname, tree_right, parent_id, hidden, placeholder, tax_related, commodity_id) values
('5e9d933c84114713991c91272e7b82f9', 'Root', 'Root', 'account', 0, 1, 'root', 'root', 12, NULL, 'f', 't', 'f', NULL),
('b55b3bdbffa046d0a3a4575cb6228691', 'Asset', 'Asset', 'account', 1, 2, 'Assets', 'Assets', 3, 1, 'f', 't', 'f', NULL),
('1903f813332e4dc7aee5af5c17c9e410', 'Liability', 'Liability', 'account', 1, 4, 'Liabilities', 'Liabilities', 5, 1, 'f', 't', 'f', NULL),
('7a532c2ec94940dea1d498806a6f4e5c', 'Income', 'Income', 'account', 1, 6, 'Income', 'Income', 7, 1, 'f', 't', 'f', NULL),
('c2e55a0e08d14f18ac24ac2149742293', 'Expense', 'Expense', 'account', 1, 8, 'Expense', 'Expense', 9, 1, 'f', 't', 'f', NULL),
('3f3f56982050451e93f64ac0edc2641a', 'Equity', 'Equity', 'account', 1, 10, 'Equity', 'Equity', 11, 1, 'f', 't', 'f', NULL);
