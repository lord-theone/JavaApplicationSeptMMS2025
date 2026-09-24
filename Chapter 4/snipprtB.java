for (k = 0.1; k != 1.0; k += 0.1)
    System.out.println(k);

//Error: The condition k != 1.0 uses floating-point equality comparison, which is unreliable. Due to the way floating-point numbers are represented in binary (IEEE 754), 0.1 cannot be represented exactly. After repeated additions, k may never equal exactly 1.0 — it could jump from 0.9999... to 1.0000...01, causing an infinite loop.

//Corrected code — use < instead of !=:
for (k = 0.1; k < 1.0; k += 0.1)
    System.out.println(k);
