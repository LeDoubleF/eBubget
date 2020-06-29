SELECT amount FROM forecast WHERE january=1;
SELECT SUM(amount) FROM forecast WHERE january=1;
SELECT SUM(amount) FROM forecast WHERE january=1 and income =TRUE;
SELECT SUM(amount) FROM forecast WHERE january=1 and income =false;
 SELECT SUM(amount) FROM forecast WHERE march=1 and income =false;
 SELECT amount,income FROM forecast WHERE march=1 ;