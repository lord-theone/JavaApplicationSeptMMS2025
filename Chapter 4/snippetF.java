switch (value % 2) {
    case 0:
        System.out.println("Even integer");
    case 1:
        System.out.println("Odd integer");
}




// corrected code
switch (value % 2) {
    case 0:
        System.out.println("Even integer");
        break;  // <-- added
    case 1:
        System.out.println("Odd integer");
}
