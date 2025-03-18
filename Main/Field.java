package Main;

/**
 * A Field Class that is used for data input on times when you are supposed
 * to input something, like for example, Bank Name and Bank Passcode.
 * @param <T> Type of the field. Can be Double, String, Integer, and all other wrapper classes.
 */
public class Field<T, E> {

    private final Class<T> fieldType;
    private final String fieldName;
    private T fieldValue;
    private final E threshold;
    private final FieldValidator<T, E> fieldValidator;

    public Field(String fieldName, Class<T> fieldType, E threshold, FieldValidator<T, E> validator) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.threshold = threshold;
        this.fieldValidator = validator;
    }

    /**
     * Get the input value of this field.
     */
    public T getFieldValue() {
        return this.fieldValue;
    }

    /**
     * Set the value for this field. Can be validated using a field validator.
     * <br>
     * Only use this method if your field input is supposedly inline. In other words, your input
     * will only receive one String.
     * @param phrase Prompt used when asking for value for this field.
     */
    public void setFieldValue(String phrase) {
        this.setFieldValue(phrase, true);
    }

    /**
     * Set the value for this field. Can be validated using a validator.
     * @param phrase Prompt used when asking for value for this field.
     * @param inlineInput A flag to ask if the input is just one entire String or receive an entire line input.
     *                    <br>
     *                    Set to true if receiving only one String input without spaces.
     *                    <br>
     *                    Set to false if receiving an entire line of String input.
     * @throws ClassCastException Prompt value returns a string originally, and must be cast to other
     * data or class types for proper input value. When the field is of type Double, Integer, or any other
     * Number class, it may result to ClassCastException as Number is not a child nor parent of String.
     * @throws NumberFormatException May happen when field is of type Double, Integer, or any number class,
     * and user suddenly inputs a String that cannot be cast into double or Integer.,
     */
    public void setFieldValue(String phrase, boolean inlineInput)
    throws ClassCastException, NumberFormatException {
        String tempval = null;
        while(true) {
            try {
                // Prompt user to input
                tempval = Main.prompt(phrase, inlineInput);
                // Cast String to a different type.
                this.fieldValue = this.fieldType.cast(tempval);
            }
            // Happens when the field is of type Double, Integer, Number, etc.
            catch(ClassCastException err) {
                try {
                    if(this.fieldType == Double.class) {
                        this.fieldValue = (T) stringToDouble(tempval);
                    }
                    else if(this.fieldType == Integer.class) {
                        this.fieldValue = (T) stringToInteger(tempval);
                    }
                }
                // This is run whenever casting is impossible as input string cannot be cast into Integer or Double
                catch(NumberFormatException err2) {
                    //
                }
            }
            finally {
                // Precautionary measure especially when NumberFormatException happens...
                if(this.fieldValue != null) {
                    String result = this.fieldValidator.validate(this.fieldValue, threshold);
                    if(result == null) break;
                    else System.out.println(result);
                }
                else {
                    System.out.println("Invalid input given!");
                }
            }
        }
    }

    private Double stringToDouble(String value) {
        return Double.parseDouble(value);
    }

    private Integer stringToInteger(String value) {
        return Integer.parseInt(value);
    }

    /**
     * A Field Validator class to compare if some double value passes some given threshold.
     * Used for validation purposes.
     */
    public static class DoubleFieldValidator implements FieldValidator<Double, Double> {

        @Override
        public String validate(Double value, Double threshold) {
            if(value < threshold) {
                return "Field input must be greater or equal to: " + threshold;
            }
            return null;
        }
    }

    /**
     * A Field Validator class to compare if some integer value passes some given threshold.
     * Used for validation purposes.
     */
    public static class IntegerFieldValidator implements FieldValidator<Integer, Integer> {

        @Override
        public String validate(Integer value, Integer threshold) {
            if(value < threshold) {
                return "Field input must be greater than or equal to: " + threshold;
            }
            return null;
        }
    }

    /**
     * A comparator class to compare if some string value is not empty.
     * Used for validation purposes.
     */
    public static class StringFieldValidator implements FieldValidator<String, String> {

        @Override
        public String validate(String value, String threshold) {
            if(value.isEmpty()) {
                return "Field cannot be empty!";
            }
            return null;
        }
    }

    /**
     * A comparator class tso compare if some string value's length is longer than
     * some threshold.
     * Used for validation purposes.
     */
    public static class StringFieldLengthValidator implements FieldValidator<String, Integer> {

        @Override
        public String validate(String value, Integer threshold) {
            if(value.length() < threshold) {
                return "Field must have at least " + threshold + " characters";
            }
            return null;
        }
    }
}
