
import java.util.Objects;
import java.io.Serializable;

public class Product implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Fixed field lengths for RandomAccessFile records
    public static final int NAME_LENGTH = 35;
    public static final int DESCRIPTION_LENGTH = 75;
    public static final int ID_LENGTH = 6;

    private String name;
    private String description;
    private final String ID;
    private double cost;

    /**
     * Constructs a Product object with the specified information.
     * @param name the product name
     * @param description the product description
     * @param ID the product ID
     * @param cost the product cost
     */
    public Product(String name, String description, String ID, double cost)
    {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.cost = cost;
    }

    /**
     * Constructs a Product object with an ID and cost.
     * @param ID the product ID
     * @param cost the product cost
     */
    public Product(String ID, double cost)
    {
        this("", "", ID, cost);
    }

    /**
     * Gets the product name.
     * @return the product name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the product description.
     * @return the product description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the product ID.
     * @return the product ID
     */
    public String getID()
    {
        return ID;
    }

    /**
     * Gets the product cost.
     * @return the product cost
     */
    public double getCost()
    {
        return cost;
    }

    /**
     * Sets the product name.
     * @param name the new product name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the product description.
     * @param description the new product description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets the product cost.
     * @param cost the new product cost
     */
    public void setCost(double cost)
    {
        this.cost = cost;
    }

    /**
     * Returns the product as a comma-separated value record.
     * @return the product's data in CSV format
     */
    public String toCSV()
    {
        return ID + ", " + name + ", " + description + ", " + cost;
    }

    /**
     * Returns the product as a JSON formatted String.
     * @return the product's data in JSON format
     */
    public String toJSON()
    {
        return "{\"ID\":\"" + ID + "\",\"name\":\"" + name
                + "\",\"description\":\"" + description
                + "\",\"cost\":" + cost + "}";
    }

    /**
     * Returns the product as an XML formatted String.
     * @return the product's data in XML format
     */
    public String toXML()
    {
        return "<Product><ID>" + ID + "</ID><name>" + name
                + "</name><description>" + description + "</description><cost>"
                + cost + "</cost></Product>";
    }

    /**
     * Returns a String representation of the Product.
     * @return a String containing the product's information
     */
    @Override
    public String toString()
    {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID='" + ID + '\'' +
                ", cost=" + cost +
                '}';
    }

    /**
     * Compares this Product with another object for equality.
     * @param obj the object to compare with this Product
     * @return true if the objects contain the same Product data
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(!(obj instanceof Product))
        {
            return false;
        }

        Product other = (Product) obj;

        return Double.compare(cost, other.cost) == 0
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(ID, other.ID);
    }

    /**
     * Returns a hash code for this Product.
     * @return a hash code based on the product's fields
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, ID, cost);
    }

    /**
     * Pads a String with spaces until it reaches the required length.
     * @param value the original String
     * @param length the required length
     * @return the padded String
     */
    private String padString(String value, int length)
    {
        if(value == null)
        {
            throw new IllegalArgumentException("String cannot be null.");
        }

        if(value.length() > length)
        {
            throw new IllegalArgumentException(
                    "String exceeds maximum length of " + length);
        }

        return String.format("%-" + length + "s", value);
    }

    /**
     * Returns the product name formatted for RandomAccessFile.
     * @return a 35-character name
     */
    public String getRandomName()
    {
        return padString(name, NAME_LENGTH);
    }

    /**
     * Returns the product description formatted for RandomAccessFile.
     * @return a 75-character description
     */
    public String getRandomDescription()
    {
        return padString(description, DESCRIPTION_LENGTH);
    }

    /**
     * Returns the product ID formatted for RandomAccessFile.
     * @return a 6-character ID
     */
    public String getRandomID()
    {
        return padString(ID, ID_LENGTH);
    }
}
