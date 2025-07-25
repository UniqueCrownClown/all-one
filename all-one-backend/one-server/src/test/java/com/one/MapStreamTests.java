package com.one;

import com.one.vo.StudentVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MapStreamTests {
    @Test
    public void testMapStream() {
        StudentClass studentClass = new StudentClass();
        studentClass.setName("三年二班");
        studentClass.setClassMates(new ArrayList<StudentVO>() {{
            add(new StudentVO("jack", 5));
            add(new StudentVO("mike", 3));
        }});
        List<StudentVO> students = studentClass.getClassMates().stream().map(item -> {
            item.setName("Mary");
            item.setAge(18);
            return item;
        }).collect(Collectors.toList());
        log.info(students.toString());
    }

    /**
     * @param null
     * @description 对象拷贝
     **/
    @Test
    public void cloneObject() {
        StudentClass studentClass = new StudentClass() {{
            setName("五年三班");
            setClassMates(new ArrayList<StudentVO>() {{
                add(new StudentVO("rose", 19));
            }});
        }};
        StudentClass studentClassTmp = new StudentClass();
        BeanUtils.copyProperties(studentClass,studentClassTmp);
        log.info(studentClassTmp.toString());
    }

}

@Data
class StudentClass {
    private String name;
    private List<StudentVO> classMates;
}