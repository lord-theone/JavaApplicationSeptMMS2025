
n = 1;
while (n < 10)
    System.out.println(n++);

//Error: The condition is n < 10 but it should be n <= 10. Since n++ is post-increment, n is printed first and then incremented. With n < 10, the loop runs while n is 1 through 9, so only 1–9 are printed. The value 10 is never printed.


// corrected code
n = 1;
while (n <= 10)
    System.out.println(n++);
