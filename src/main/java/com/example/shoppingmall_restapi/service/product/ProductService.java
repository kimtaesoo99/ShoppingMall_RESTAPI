package com.example.shoppingmall_restapi.service.product;

import com.example.shoppingmall_restapi.dto.product.ProductCreateRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductEditRequestDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindAllResponseDto;
import com.example.shoppingmall_restapi.dto.product.ProductFindResponseDto;
import com.example.shoppingmall_restapi.entity.image.Image;
import com.example.shoppingmall_restapi.entity.likes.Likes;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.exception.MemberNotEqualsException;
import com.example.shoppingmall_restapi.exception.ProductNotFoundException;
import com.example.shoppingmall_restapi.repository.likes.LikesRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.service.image.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@RequestMapping("api")
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    private final LikesRepository likesRepository;

    //상품 등록
    @Transactional
    public void productCreate(ProductCreateRequestDto req , Member member){
        List<Image> images = req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(toList());
        Product product = new Product(req.getName(),req.getComment(),req.getPrice(), req.getQuantity(), member,0,0,images);
        productRepository.save(product);
        uploadImages(product.getImages(), req.getImages());


    }
    //상품 전체조회
    @Transactional(readOnly = true)
    public List<ProductFindAllResponseDto> productFindAll(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductFindAllResponseDto> result = new ArrayList<>();
        products.forEach(s-> result.add(ProductFindAllResponseDto.toDto(s)));
        return result;
    }
    //상품 단건 조회
    @Transactional(readOnly = true)
    public ProductFindResponseDto productFind(Long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductFindResponseDto.toDto(product);
    }
    //상품 수정
    @Transactional
    public void productEdit(ProductEditRequestDto req, Long id, Member member){
        Product product =productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (!member.equals(product.getSeller()))throw new MemberNotEqualsException();
        Product.ImageUpdatedResult result = product.ProductEdit(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());

    }

    //상품 삭제
    @Transactional
    public void productDelete(Long id, Member member){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (!member.equals(product.getSeller()))throw new MemberNotEqualsException();
        productRepository.delete(product);
    }

    //상품 좋아요 및 취소
    @Transactional
    public void productLike(Long id ,Member member){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (!likesRepository.findByMemberAndProduct(member,product).isEmpty()){
            Likes likes = likesRepository.findByMemberAndProduct(member,product).get();
            likesRepository.delete(likes);
            product.setLikesCount(product.getLikesCount()-1);
        }
        else {
            likesRepository.save(new Likes(member,product));
            product.setLikesCount(product.getLikesCount()+1);
        }

    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }


}
