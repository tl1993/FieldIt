package com.example.dell.fieldit.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by galna21 on 12/08/2017.
 */

public class Model {
    public final static Model instace = new Model();

    private Model(){
        Field fd = new Field ("1", "Gal", "Grass", "31.771959", "35.217018", "cool field");
        data.add(fd);

        Field fd2 = new Field("2", "Tomer", "Grass", "32.109333 ", "34.855499", "cool field");
        data.add(fd2);

        Field fd3 = new Field("3", "Ravit", "Grass", "31.771954", "35.217013", "cool field");
        data.add(fd3);

    }

    private List<Field> data = new LinkedList<Field>();

    public List<Field> getAllFields(){
        return data;
    }

//    public void addField(Field fd){
//        data.add(fd);
//    }
//
//    public Field getField(String fbId) {
//        for (Field s : data){
//            if (s.id.equals(stId)){
//                return s;
//            }
//        }
//        return null;
//    }
//
//    public void updateStudent( int position,Field newSt) {
//        data.set(position,newSt);
//    }
//
//    public void removeStudent(Field st) {
//        data.remove(st);
//    }
// }
}
