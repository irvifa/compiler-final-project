import java.util.LinkedList;

class Bucket {

    public static final int INTEGER = 0;
    public static final int BOOLEAN = 1;
    public static final int UNDEFINED = -1;

    public static final int SCALAR = 0;
    public static final int ARRAY = 1;
    public static final int PROCEDURE = 2;
    public static final int FUNCTION = 3;

    private String idName;
    private int orderNum;
    private int lexicLev;
    private int idType;
    private int idKind;
    private Bucket nextBucket;
    
    // Add base address
    private int baseAddress;

    // Add list of params
    private LinkedList<Bucket> listOfParams;
    
    // Add params count
    private int numberOfParams;

    public Bucket() {}

    public Bucket(String idName) {
        this.idName = idName;
        this.lexicLev = UNDEFINED;
        this.orderNum = UNDEFINED;
        this.idType = UNDEFINED;
        this.idKind = UNDEFINED;
        this.baseAddress = UNDEFINED;
        this.nextBucket = null;
        this.listOfParams = null;
        this.numberOfParams = 0;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setLexicLev(int lexicLev) {
        this.lexicLev = lexicLev;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public void setLLON(int lexicLev, int orderNum) {
        this.lexicLev = lexicLev;
        this.orderNum = orderNum;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setIdKind(int idKind) {
        this.idKind = idKind;
    }

    public void setNextBucket() {
        nextBucket = null;
    }

    public void setNextBucket(Bucket next) {
        nextBucket = next;
    }

    public String getIdName() {
        return idName;
    }

    public int getLexicLev() {
        return lexicLev;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getIdType() {
        return idType;
    }

    public int getIdKind() {
        return idKind;
    }

    public String getIdTypeStr() {
        String type = null;

        switch (idType) {
            case INTEGER:
                type = "integer";
                break;
            case BOOLEAN:
                type = "boolean";
                 break;
            case UNDEFINED:
                type = "undefined";
                break;
        }

        return type;
    }

    public String getIdKindStr() {
        String kind = null;

        switch (idKind) {
            case SCALAR:
                kind = "scalar";
                break;
            case ARRAY:
                kind = "array";
                break;
            case PROCEDURE:
                kind = "procedure";
                break;
            case FUNCTION:
                kind = "function";
                break;
            case UNDEFINED:
                kind = "undefined";
                break;
        }

        return kind;
    }


    public Bucket getNextBucket() {
        return nextBucket;
    }

    // Add setter for baseAddress
    public void setBaseAddress(int baseAddress) {
        this.baseAddress = baseAddress;
    }

    // Add getter for baseAddress
    public int getBaseAddress() {
        return this.baseAddress;
    }

    // Add setter for listOfParams
    public void setListOfParams(LinkedList<Bucket> listOfParams) {
        this.listOfParams = listOfParams;
    }

    // Add getter for list of params
    public LinkedList<Bucket> getListOfParams() {
        return this.listOfParams;
    }

    public void setNumberOfParams(int numberOfParams) {
        this.numberOfParams = numberOfParams;
    }

    public int getNumberOfParams() {
        return this.numberOfParams;
    }

}