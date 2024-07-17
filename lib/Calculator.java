package lib;

public class Calculator {
  public static double calc (String operation) {
    // remove white spaces
    String op = operation.replaceAll("\\s", "");

    // discover operator
    char operator = ' ';
    int i = -1;
    for (i = 0; i < op.length(); i++) {
      if (op.charAt(i) == '+' || op.charAt(i) == '-' || op.charAt(i) == '*' || op.charAt(i) == '/' || op.charAt(i) == '%') {
        operator = op.charAt(i);
        break;
      }
    }

    if (operator == ' ') {
      return 0;
    }

    // get operands
    String a = op.substring(0, i);
    String b = op.substring(i + 1);

    try {
      double num1 = Double.parseDouble(a);
      double num2 = Double.parseDouble(b);

      switch (operator) {
        case '+':
          return num1 + num2;
        case '-':
          return num1 - num2;
        case '*':
          return num1 * num2;
        case '/':
          if (num2 == 0) {
            return 0;
          } else {
            return num1 / num2;
          }
        case '%':
          return num1 % num2;
        default:
          return 0;
      }
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
