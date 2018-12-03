package formate;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class FormaterTest {
    Formatter formater = null;
    StringBuilder sb = null;

    @Before
    public void init(){
        sb = new StringBuilder();
        formater = new Formatter(sb, Locale.CHINA);
    }

    @Test
    public void test1(){


        formater.format("%4$2s %3$2s %2$2s %1$2s\n", "a", "b", "c", "d");
//        System.out.println(sb);

        System.out.println(Math.E);
        formater.format(Locale.CHINA, "e= %+10.4f\n", Math.E);
        System.out.println(sb);
    }

    @Test
    public void test2(){

        formater.format("Amount gained or lost since last statement: $ %(,.2f", 6217.58);
        System.out.println(formater);
    }

    @Test
    public void test3(){
        Calendar instance = Calendar.getInstance();
        System.out.println(instance);
        formater.format("Local time: %tT", instance);
        System.out.println(sb);
    }

    @Test
    public void test4() throws ParseException {
        Date date = new Date();
        String strDate = "1998-02-03";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = dateFormat.parse(strDate);
        formater.format("%10s %tT %tT", "xx", date, parse);
        System.out.println(sb);


    }


}
