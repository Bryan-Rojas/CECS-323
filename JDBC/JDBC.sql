/*

Project Name: JDBC
Authors: Bryan Rojas, Mateo Perez, Peter Park
Due Date: 3/18/2019

*/

/***************************************************************************************************************

WritingGroup table consists of the groups that have written a book, the head writers, the year that the groups were formed, and their subject matter. 

A writing group is a group known for writing a specific book.

***************************************************************************************************************/
CREATE TABLE WritingGroup
(
groupName VARCHAR(30) NOT NULL,
headWriter VARCHAR(30),
yearFormed INTEGER,
subject VARCHAR(30),
CONSTRAINT pk_WritingGroups PRIMARY KEY (groupName)
);



/***************************************************************************************************************

Publisher table consists of the publisher's name, address, phone number, and email. 

A publisher is a company that licenses and publishes books for mass production and distribution.

***************************************************************************************************************/
CREATE TABLE Publisher 
(
publisherName varchar(30) NOT NULL,
publisherAddress varchar(30),
publisherPhone varchar(15),
publisherEmail varchar(30),
CONSTRAINT pk_Publisher PRIMARY KEY (publisherName)
);



/***************************************************************************************************************

Book table consists of the group's name who wrote it, the title of the book, the publisher's name, the year published, and the total number of pages.

A book is a written or printed work that is glued together along one edge and binded together by two covers.

***************************************************************************************************************/
CREATE TABLE Book
(
groupName VARCHAR(30) NOT NULL,
bookTitle VARCHAR(30) NOT NULL,
publisherName VARCHAR(30) NOT NULL,
yearPublished INTEGER,
numberPages INTEGER,
CONSTRAINT pk_Book PRIMARY KEY (groupName, bookTitle)
);


/***************************************************************************************************************

Alterations needed for Book table to add foreign key constraints.

***************************************************************************************************************/
ALTER TABLE Book
        ADD CONSTRAINT Books_WrittingGroup_fk
        FOREIGN KEY (groupName)
        REFERENCES WritingGroup(groupName);

ALTER TABLE Book
        ADD CONSTRAINT Books_Publiher_fk
        FOREIGN KEY (publisherName)
        REFERENCES Publisher(publisherName);



/***************************************************************************************************************

Data for WritingGroups

***************************************************************************************************************/
INSERT INTO WritingGroup (groupName,headWriter,yearFormed,subject) VALUES
('Elohim', 'El', 2016, 'Music'),
('New Republic','Cali', 2019,'Government'),
('A Team','Hogan', 1985,'Action'),
('Evolution','Hunter',2005,'Anthropology'),
('Squad 7','Welkin',2007, 'Military'),
('Squad 8','Fabio',2007,'Military'),
('Walruses','Mateo',2013,'Therapy'),
('Super Friends','Peter',2018,'Computer Science'),
('Time','Tim',1995,'Literature'),
('TriCell','Thomas',1700,'Biology'),
('Umbrella','Spencer',1980,'Chemistry'),
('Blue Umbrella','Veronica',1998,'Chemistry');



/***************************************************************************************************************

Data for Publishers

***************************************************************************************************************/
INSERT INTO Publisher (publisherName, publisherAddress, publisherPhone, publisherEmail) VALUES
('Oxford University Press','123 Pine Street','759-365-8395','OFP@gmail.com'),
('Cambridge University Press','8786 Oak Ave','964-247-3475','CUP@gmail.com'),
('Elsevier','9732 Cherry St','562-911-6969','elsevier@gmail.com'),
('Simon and Schuster','32562 North L St','213-475-6900','SiMSch@gmail.com'),
('Houghton Mifflin Harcourt','86235 S Nectarine','310-546-9973','HMH@gmail.com'),
('Harpercollins','231 Laurel St','987-234-3478','Harper@gmail.com'),
('Wiley','329 North H St','694-959-6362','Wiley@gmail.com'),
('Chronicle Books','9000 Earl Burns Drive','423-450-4200','ChronicleBooks@gmail.com'),
('Random House','911 Impala St','111-111-1111','randomhouse@gmail.com'),
('Pearson','32489 Peach Ave','222-222-2222','pearson@gmail.com'),
('Cengage','7823 Beast Dr','333-333-3333','cengage@yahoo.com'),
('McGraw-Hill Edu','1000 E Pond Dr','444-444-4444','McGrawHill@gmail.com');



/***************************************************************************************************************

Data for Books

***************************************************************************************************************/
INSERT INTO Book(groupName, bookTitle, publisherName, yearPublished, numberPages) VALUES
('Elohim', 'Sensations', 'Oxford University Press', 2016, 200),
('Elohim', 'Sensations 2', 'Oxford University Press', 2017, 400),
('Elohim', 'Sensations: Revenge', 'Cambridge University Press', 2018, 600),

('New Republic', 'Cali Reimagined', 'Cambridge University Press', 2019, 300),
('New Republic', 'Cali Destroyed', 'Houghton Mifflin Harcourt', 2020, 350),

('A Team', 'Bad Man', 'Elsevier', 1989, 305),
('A Team', 'Bad Man: Prequel', 'Elsevier', 1999, 65),

('Evolution', 'The Betrayal','Simon and Schuster', 2005, 267),
('Evolution', 'The Betrayal of My Publisher', 'Simon and Schuster', 2008, 127),

('Squad 7', 'Art of War', 'Houghton Mifflin Harcourt', 2019, 1),

('Squad 8', 'Better Art of War', 'Harpercollins', 2010, 150),
('Squad 8', 'Better Art of War 2', 'Harpercollins', 2011, 250),
('Squad 8', 'Better Art of War vs Vampires', 'Harpercollins', 2012, 350),
('Squad 8', 'Better Art of War vs Zombies', 'Harpercollins', 2013, 450),
('Squad 8', 'Better Art of War vs Code', 'Harpercollins', 2014, 550),
('Squad 8', 'Better Art of War Reprised', 'Harpercollins', 2015, 650),

('Walruses', 'Friendship','Wiley', 2016, 205),

('Super Friends', 'How to Write Good Code', 'Chronicle Books', 2018, 500),
('Super Friends', 'How to Write Better Code', 'Chronicle Books', 2019, 1000),
('Super Friends', 'How to Write Bestest Code', 'Chronicle Books', 2049, 1500),

('Time', 'Time is Money','Random House', 1996, 1000),

('TriCell', 'Ouroboros Virus','Pearson', 1802, 289),

('Umbrella', 'How to Make Zombies', 'Cengage', 1985, 515),
('Umbrella', 'How to Make Cute Zombies', 'Cengage', 1995, 215),

('Blue Umbrella','How to Kill Zombies','McGraw-Hill Edu', 1999, 600);