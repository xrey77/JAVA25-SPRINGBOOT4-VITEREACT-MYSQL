package com.java25.java25.springboot4.mysql.repository;

import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.dto.ProfileDto;
import com.java25.java25.springboot4.mysql.dto.RegisterDto;
import com.java25.java25.springboot4.mysql.dto.SaleDto;
import com.java25.java25.springboot4.mysql.dto.UserlistDto;
import com.java25.java25.springboot4.mysql.entities.Product;
import com.java25.java25.springboot4.mysql.entities.Sale;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.entities.Role; // Added import
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    ProductDto toProductDto(Product product);     
    List<ProductDto> toProductDtoList(List<Product> products);    
    
    SaleDto toSaleDto(Sale sale);
    List<SaleDto> toSaleDtoList(List<Sale> sales);

    List<UserlistDto> toDtoList(List<User> users);    
    void updateUserFromDto(RegisterDto registerDto, @MappingTarget User user);  
    void updateUserFromProfileDto(ProfileDto profileDto, @MappingTarget User user);    

    default String mapRolesToString(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .map(Role::getName) 
                .collect(Collectors.joining(", "));
    }
}
