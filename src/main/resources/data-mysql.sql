INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (1, 'Yoga Basics', 'Full-Body Yoga for Beginners', 'Easy', 400.00, true, 15, 1);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (2, 'Get In Shape', 'A Mix of Cardio and Full-Body Toning', 'Medium', 367.00, true, 10, 1);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (3, 'Pure Pilates', 'Holistic Training for Posture and Movement', 'Easy', 290.00, true, 8, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (4, 'Fat Burner', 'Fat-Burning Workouts to Get You Lean', 'Medium', 280.00, true, 6, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (5, 'Body Shape', 'Total-Body Toning', 'Medium', 310.00, true, 12, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (6, 'Six Pack', 'Core Strength Training for Sculpted Abs', 'Hard', 430.00, true, 12, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (7, 'Yoga Recover', 'Calming and Relaxing Restorative Yoga Flows', 'Easy', 180.00, true, 6, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (8, 'Full Body Circuit', 'Full-Body Dumbbell Circuit Training for Beginners', 'Easy', 384.00, true, 9, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (9, 'Mobility', 'Dynamic Stretches to Increase Flexibility and Mobility', 'Medium', 340.00, true, 7, 2);
INSERT INTO Subscription.product (productId, name, description, trainingLevel, price, status, durationInWeeks, accessType)  VALUES (10, 'Full-Body Starter', 'A 4-Week Program Perfect for Beginners', 'Easy', 130.00, true, 4, 1);


INSERT INTO Subscription.customer (customerId, name, email, password, phone, address, active, role) VALUES (1, 'Donald Duck', 'donald@gmail.com', 'duck', '7866554422', '230, West road, Berlin', true, 'ROLE_CUSTOMER');
INSERT INTO Subscription.customer (customerId, name, email, password, phone, address, active, role) VALUES (2, 'Leonardo D', 'leo@gmail.com', 'caprio', '7866554422', '230, West road, Berlin', true, 'ROLE_USER');
INSERT INTO Subscription.customer (customerId, name, email, password, phone, address, active, role) VALUES (3, 'Tom Cruise', 'tom@gmail.com', 'mission', '7866554422', '230, West road, Berlin', true, 'ROLE_CUSTOMER_ON_TRAIL');
INSERT INTO Subscription.customer (customerId, name, email, password, phone, address, active, role) VALUES (4, 'Robin Goodman', 'robin@gmail.com', 'batman', '7866554422', '230, West road, Berlin', true, 'ROLE_CUSTOMER');


INSERT INTO Subscription.voucher (voucherId, code, description, percentageDiscount, expiryTimestamp) VALUES (1, 'FLAT10', 'Flat 10% off', 10, '2021-02-14 11:59:59');
INSERT INTO Subscription.voucher (voucherId, code, description, percentageDiscount, expiryTimestamp) VALUES (2, 'NY25OFF', 'New year sale! 25% Off', 25, '2021-02-28 11:59:59');
INSERT INTO Subscription.voucher (voucherId, code, description, percentageDiscount, expiryTimestamp) VALUES (3, 'GET5D', '5% Discount, Hurry!', 5, '2021-01-28 12:00:00');


INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (1, 1);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (1, 2);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (1, 3);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (2, 4);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (2, 5);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (2, 6);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (3, 7);
INSERT INTO Subscription.voucher_product (vouchers_voucherId, products_productId) VALUES (3, 8);