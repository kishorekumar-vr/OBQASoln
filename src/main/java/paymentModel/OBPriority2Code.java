package paymentModel;


public enum OBPriority2Code {
    NORMAL("Normal"),
    URGENT("Urgent");

    private String value;

    OBPriority2Code(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }

    public String getValue(){
        return value;
    }
}
