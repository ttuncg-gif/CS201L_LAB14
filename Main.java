import java.io.*;
import java.text.DecimalFormat;

class Node {
    double data;
    Node next;

    Node(double value) {
        data = value;
        next = null;
    }
}

class LinkedList {
    protected Node head;

    LinkedList() {
        head = null;
    }

    void pushFront(double value) {
        Node temp = new Node(value);
        temp.next = head;
        head = temp;
    }

    double popFront() {
        if (head == null)
            return 0;
        Node temp = head;
        double value = temp.data;
        head = head.next;
        return value;
    }

    boolean isEmpty() {
        return head == null;
    }
}

class StackLL extends LinkedList {
    void push(double value) {
        pushFront(value);
    }

    boolean pop(Wrapper value) {
        if (isEmpty())
            return false;
        value.x = popFront();
        return true;
    }
}

class Wrapper {
    double x;
}

public class Main {
    public static boolean isNumber(String token, Wrapper value) {
        try {
            value.x = Double.parseDouble(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/");
    }

    public static boolean evaluatePostfix(String line, Wrapper result, Wrapper errorFlag, StringBuilder errorMessage) {
        StackLL stack = new StackLL();
        String[] tokens = line.split("\\s+");
        errorMessage.setLength(0);

        for (String token : tokens) {
            Wrapper val = new Wrapper();

            if (isNumber(token, val)) {
                stack.push(val.x);
            } else if (isOperator(token)) {
                Wrapper right = new Wrapper();
                Wrapper left = new Wrapper();

                if (!stack.pop(right) || !stack.pop(left)) {
                    errorMessage.append("Too few operands");
                    errorFlag.x = 1;
                    return false;
                }

                if (token.equals("+"))
                    result.x = left.x + right.x;
                else if (token.equals("-"))
                    result.x = left.x - right.x;
                else if (token.equals("*"))
                    result.x = left.x * right.x;
                else if (token.equals("/")) {
                    if (right.x == 0) {
                        errorMessage.append("Divide by zero issue");
                        errorFlag.x = 1;
                        return false;
                    }
                    result.x = left.x / right.x;
                }

                stack.push(result.x);
            } else {
                errorMessage.append("Contains a token error");
                errorFlag.x = 1;
                return false;
            }
        }

        Wrapper finalVal = new Wrapper();
        if (!stack.pop(finalVal)) {
            errorMessage.append("Too few operands");
            errorFlag.x = 1;
            return false;
        }

        if (!stack.isEmpty()) {
            errorMessage.append("Too few operators");
            errorFlag.x = 1;
            return false;
        }

        result.x = finalVal.x;
        return true;
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("0.00");

        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter("report.txt"));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                Wrapper result = new Wrapper();
                Wrapper errorFlag = new Wrapper();
                StringBuilder errorMessage = new StringBuilder();

                pw.print("Postfix Expression: " + line);

                if (evaluatePostfix(line, result, errorFlag, errorMessage)) {
                    pw.println("     = " + df.format(result.x));
                } else {
                    pw.println("     " + errorMessage.toString());
                }
            }

            br.close();
            pw.close();

        } catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
