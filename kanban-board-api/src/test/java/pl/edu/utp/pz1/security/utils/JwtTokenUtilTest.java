package pl.edu.utp.pz1.security.utils;

import org.junit.Assert;
import org.junit.Test;
import pl.edu.utp.pz1.model.Student;

public class JwtTokenUtilTest {

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("test-secret-key");

    private String generateToken() {
        Student student = new Student();
        student.setEmail("student@test.pl");
        return jwtTokenUtil.generateToken(student);
    }

    @Test
    public void test_getUsernameFromToken() {
        String token = generateToken();
        String expected = "student@test.pl";
        String actual = jwtTokenUtil.getUsernameFromToken(token);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_isTokenExpired() {
        String token = generateToken();
        Assert.assertFalse(jwtTokenUtil.isTokenExpired(token));
    }

    @Test
    public void test_validateToken_invalid() {
        String token = generateToken();
        Student student = new Student();
        student.setEmail("student2@test.pl");
        Assert.assertFalse(jwtTokenUtil.validateToken(token, student));
    }

    @Test
    public void test_validateToken_valid() {
        String token = generateToken();
        Student student = new Student();
        student.setEmail("student@test.pl");
        Assert.assertTrue(jwtTokenUtil.validateToken(token, student));
    }

}
