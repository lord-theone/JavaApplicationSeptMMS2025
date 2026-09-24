for (int i = 1; i <= 10; i++) {
    for (int j = 1; j <= 5; j++)
        System.out.print('@');
    System.out.println();
}

//Answer: The program prints a rectangle of '@' characters that is 10 rows tall and 5 columns wide. The outer loop runs 10 times (one per row). For each row, the inner loop prints 5 '@' characters side by side. After each row, System.out.println() moves the cursor to the next line. The output looks like this:
