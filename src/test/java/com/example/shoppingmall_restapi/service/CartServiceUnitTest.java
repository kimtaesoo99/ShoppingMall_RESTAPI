package com.example.shoppingmall_restapi.service;

import com.example.shoppingmall_restapi.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall_restapi.dto.cart.CartItemResponseDto;
import com.example.shoppingmall_restapi.entity.cart.Cart;
import com.example.shoppingmall_restapi.entity.cartitem.CartItem;
import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.repository.cart.CartItemRepository;
import com.example.shoppingmall_restapi.repository.cart.CartRepository;
import com.example.shoppingmall_restapi.repository.history.HistoryRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import com.example.shoppingmall_restapi.service.cart.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall_restapi.factory.CartFactory.createCart;
import static com.example.shoppingmall_restapi.factory.CartFactory.createCartItem;
import static com.example.shoppingmall_restapi.factory.HistoryFactory.createHistory;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createMember;
import static com.example.shoppingmall_restapi.factory.MemberFactory.createSeller;
import static com.example.shoppingmall_restapi.factory.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {
    @InjectMocks
    CartService cartService;
    @Mock
    CartRepository cartRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    HistoryRepository historyRepository;

    @Test
    @DisplayName("장바구니 담기")
    public void cartCreateTest(){
        //given
        CartCreateRequestDto req = new CartCreateRequestDto(1l,1);
        Member member = createMember();
        Member seller = createSeller();
        Product product =createProduct(seller);
        Cart cart = createCart(member);
        given(productRepository.findById(req.getProduct_id())).willReturn(Optional.of(product));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));

        //when
        cartService.cartCreate(req,member);

        //then
        verify(cartItemRepository).save(any());
    }

    @Test
    @DisplayName("장바구니 조회")
    public void cartFindAllTest(){
        //given
        Member member =  createMember();
        Cart cart = createCart(member);
        Product product = createProduct(member);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(createCartItem(cart,product));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByCart(cart)).willReturn(cartItems);

        //when
        List<CartItemResponseDto> result = cartService.cartFindAll(member);

        //then
        assertThat(result.size()).isEqualTo(cartItems.size());
    }
    @Test
    @DisplayName("장바구니 품목 삭제")
    public void  cartDelete(){
        //given
        Long cartItemId = 1l;
        Member member =createMember();
        Cart cart = createCart(member);
        CartItem cartItem = createCartItem(cart,createProduct(member));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(cartItem));

        //when
        cartService.cartDelete(cartItemId,member);

        //then
        verify(cartItemRepository).deleteById(cartItemId);
    }

    @Test
    @DisplayName("전체 구매")
    public void buyingTest(){
        //given
        Member member =createMember();
        member.setMoney(1000);
        Member seller = createSeller();
        Cart cart = createCart(member);
        Product product = createProduct(seller);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(createCartItem(cart,product));
        given(cartRepository.findCartByMember(member)).willReturn(Optional.of(cart));
        given(cartItemRepository.findAllByCart(cart)).willReturn(cartItems);
        //when
        cartService.buyingAll(member);


        //then
        verify(cartRepository).delete(any());
    }

}
