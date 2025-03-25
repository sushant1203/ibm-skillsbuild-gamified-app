# New default branch

Setting Up MySQL

1.Open MySQL and log in as the root user.

2.Create a new database schema named skillsyncdb by running the following command in MySQL: CREATE DATABASE skillsyncdb;

3.Go to Administration > Users and Privileges in MySQL Workbench.

4.Create a new user with the following credentials:
Username: skillsync
Password: 123456789

5.Assign the Administration role and grant all privileges to this user.

6.Ensure MySQL service is running before starting the application.

Setting Up in IntelliJ

1.open IntelliJ and navigate to Database.

2.Click the Add Database button.

3.Select MySQL as the database type.

4.Enter the following credentials:

User: skillsync

Password: 123456789

Database Name: skillsyncdb

5.Click Test Connection to verify the setup.

6.Once successful, click Apply to save the configuration.
