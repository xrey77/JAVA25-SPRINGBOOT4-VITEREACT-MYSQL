package com.java25.java25.springboot4.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java25.java25.springboot4.mysql.entities.Product;
import com.java25.java25.springboot4.mysql.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String username);
//    @Query("SELECT u FROM User u WHERE u.username = :username")
//    public User getUserByUsername(@Param("username") String username);
	
    @Query(value = "SELECT id,role_id,firstname,lastname,email,mobile,username,isactivated,isblocked,mailtoken,userpic,secret,qrcodeurl FROM User", nativeQuery = true)
    List<Product> listUsers();
	
	
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("emailadd") String emailadd);

    @Query("DELETE FROM User u WHERE u.id = :id")
    public User deleteByUser(@Param("id") int id);
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
	
}
