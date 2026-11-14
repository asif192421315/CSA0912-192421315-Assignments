public class FeeService {
    public String payFee(String regNo, String amountText) {
        if (regNo == null || regNo.trim().isEmpty())
            return "Registration number is required";
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            return "Enter a valid amount";
        }
        if (amount <= 0) return "Amount must be greater than zero";
        // Demonstration only. Replace with a real repository/payment gateway.
        return "Payment successful: Rs. " + amount;
    }

    public String generateReceipt(String regNo) {
        if (regNo == null || regNo.trim().isEmpty())
            return "Enter registration number";
        return "Receipt generated for " + regNo;
    }
}
