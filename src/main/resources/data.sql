-- managers --
INSERT INTO manager (id, first_name, middle_name, last_name, phone, alternate_id)
    VALUES(1, 'fName1', 'mName1', 'lName1', '111-111-111', NULL);

INSERT INTO manager (id, first_name, middle_name, last_name, phone, alternate_id)
    VALUES(2, 'fName2', 'mName2', 'lName2', '222-222-222', 1);

INSERT INTO manager (id, first_name, middle_name, last_name, phone, alternate_id)
    VALUES(3, 'fName3', 'mName3', 'lName3', '333-333-333', NULL);

INSERT INTO manager (id, first_name, middle_name, last_name, phone, alternate_id)
    VALUES(4, 'fName4', 'mName4', 'lName4', '444-444-444', 1);

INSERT INTO manager (id, first_name, middle_name, last_name, phone, alternate_id)
    VALUES(5, 'fName5', 'mName5', 'lName5', '555-555-555', NULL);

-- clients --
INSERT INTO client (id, name, address, manager_id)
    VALUES (1, 'client1', 'address1', 1);

INSERT INTO client (id, name, address, manager_id)
    VALUES (2, 'client2', 'address2', 2);

INSERT INTO client (id, name, address, manager_id, deleted)
    VALUES (3, 'client3', 'address3', 2, TRUE);

INSERT INTO client (id, name, address, manager_id)
    VALUES (4, 'client4', 'address4', 4);