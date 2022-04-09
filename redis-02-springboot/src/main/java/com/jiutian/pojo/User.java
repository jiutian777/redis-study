package com.jiutian.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ClassName:User
 * Package:com.jiutian.pojo
 * Description:
 *
 * @Date:2021/11/24 19:14
 * @Author:jiutian
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private String name;
    private int age;
}
