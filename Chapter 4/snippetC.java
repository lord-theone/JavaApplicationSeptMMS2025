switch (n) {
    case 1:
        System.out.println("The number is 1");
    case 2:
        System.out.println("The number is 2");
        break;
    default:
        System.out.println("The number is not 1 or 2");
        break;
}

//Error: case 1 is missing a break statement. Without break, execution falls through into case 2 after printing "The number is 1". So if n == 1, both "The number is 1" AND "The number is 2" will be printed.

// corrected code
switch (n) {
    case 1:
        System.out.println("The number is 1");
        break;  // <-- added
    case 2:
        System.out.println("The number is 2");
        break;
    default:
        System.out.println("The number is not 1 or 2");
        break;
}
