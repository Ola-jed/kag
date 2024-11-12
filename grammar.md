# Grammar for the parsing of monomials / polynomials from strings


```bnf
<input>                  ::= <polynomial> | <monomial>

<polynomial>             ::= <term>+
<term>                   ::= {[<coefficient>]<monomial>}
<monomial>               ::= <single_term_expression>+

<coefficient>            ::= { <digit> } ["." { <digit> }]
<single_term_expression> ::= <indeterminate>["^"<integer>]
<indeterminate>          ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m"
                                 | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y"
                                 | "z" | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K"
                                 | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W"
                                 | "X" | "Y" | "Z" |
<integer>                ::= { <digit> }
<digit>                  ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```
