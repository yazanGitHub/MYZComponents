
package com.myz.component;
import javafx.scene.Node;
/**
 *
 * @author Montazar Hamoud 12.5.2020
 */
public interface myzComponent
{

    public static final String CLASS_ERROR = "error";


    public void refreshCaption();
    public void removeData();

    public void setIsMandatory(boolean isMandatory);
    public boolean getIsMandatory();
    public boolean checkEmptyAndMandatory();

    public void resetStyle();

    public void   setFieldName(String fieldName);
    public String getFieldName();
    public void   setCaption(String caption);
    public String getCaption();
    public String getSQLWhere();
    public String getSQLWhereBefor();
    public String getSQLWhereAfter();
    public Object getValue();
    public Node   getNode();
}
