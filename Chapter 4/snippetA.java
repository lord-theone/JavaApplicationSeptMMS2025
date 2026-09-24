
i = 1;
while (i <= 10);
    ++i;
}

//Error 1: There is a stray semicolon (;) at the end of the while line. This makes the while loop body an empty statement — the loop spins forever doing nothing, causing an infinite loop.
//Error 2: The opening brace { for the while block is missing. Only a closing } is present, which causes a compile error.

//Corrected code:
i = 1;
while (i <= 10) {
    ++i;
}
