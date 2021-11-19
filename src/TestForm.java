/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamza
 */

public class TestForm {
    private int type;
    private char answer;
    private String theQues;
    private String choices[] = new String[4];
    private String quesPic;
    private String ansPicA, ansPicB, ansPicC, ansPicD;
    private boolean selected[] = new boolean[4];

    public TestForm(int t, char a, String tq, String c[], String qp, String apA, String apB, String apC, String apD) {
        type = t;
        answer = a;
        theQues = tq;
        choices = c.clone();
        quesPic = qp;
        ansPicA = apA;
        ansPicB = apB;
        ansPicC = apC;
        ansPicD = apD;
        for (int k = 1; k < 4; k++) {
            selected[k] = false;
        }
    }

    public int getType() {
        return type;
    }

    public String getTheQues() {
        return theQues;
    }

    public String getQuesPic() {
        return quesPic;
    }

    public String getAnsPicA() {
        return ansPicA;
    }

    public String getAnsPicB() {
        return ansPicB;
    }

    public String getAnsPicC() {
        return ansPicC;
    }

    public String getAnsPicD() {
        return ansPicD;
    }

    public String getChoice(int no) {
        return choices[no];
    }

    public boolean getSelected(int no) {
        return selected[no];
    }

    public void setSelected(int no, boolean status) {
        selected[no] = status;
    }
}
