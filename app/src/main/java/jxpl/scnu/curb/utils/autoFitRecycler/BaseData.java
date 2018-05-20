package jxpl.scnu.curb.utils.autoFitRecycler;

/**
 * created on ${date}
 *
 * @author iri-jwj
 * @version 1 init
 */
public abstract class BaseData {
    protected String str_id;

    public BaseData(String para_id) {
        str_id = para_id;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String para_id) {
        str_id = para_id;
    }
}
