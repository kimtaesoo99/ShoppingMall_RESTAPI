package com.example.shoppingmall_restapi.service.cart;

import com.example.shoppingmall_restapi.dto.cart.CartCreateRequestDto;
import com.example.shoppingmall_restapi.dto.cart.CartItemResponseDto;
import com.example.shoppingmall_restapi.entity.cart.Cart;
import com.example.shoppingmall_restapi.entity.cartitem.CartItem;
import com.example.shoppingmall_restapi.entity.history.History;
import com.example.shoppingmall_restapi.entity.member.Member;
import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.exception.*;
import com.example.shoppingmall_restapi.repository.cart.CartItemRepository;
import com.example.shoppingmall_restapi.repository.cart.CartRepository;
import com.example.shoppingmall_restapi.repository.history.HistoryRepository;
import com.example.shoppingmall_restapi.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final HistoryRepository historyRepository;
    //장바구니 담기
    @Transactional
    public void cartCreate(CartCreateRequestDto req, Member member){
        Product product = productRepository.findById(req.getProduct_id()).orElseThrow(ProductNotFoundException::new);

        if (product.getQuantity()<req.getQuantity())throw new LakingOfProductQuantity();
        //장바구니 만들기
        if (cartRepository.findCartByMember(member).isEmpty()){
            Cart cart = new Cart(member);
            cartRepository.save(cart);
        }
        Cart cart = cartRepository.findCartByMember(member).get();
        CartItem cartItem = new CartItem(cart,product,req.getQuantity());
        cartItemRepository.save(cartItem);
    }

    //장바구니 조회
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> cartFindAll(Member member){
        Cart cart = cartRepository.findCartByMember(member).orElseThrow(CartNotFoundException::new);
        List<CartItem> items = cartItemRepository.findAllByCart(cart);
        List<CartItemResponseDto> result = new ArrayList<>();

        for (CartItem item : items){
            Product product = item.getProduct();
            result.add(new CartItemResponseDto().toDto(item,product.getName(),product.getPrice()));
        }
        return  result;
    }
    //장바구니 품목 단건 삭제
    @Transactional
    public void cartDelete(Long cartItemId,Member member){
        Cart cart = cartRepository.findCartByMember(member).orElseThrow(CartNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(CartItemNotFoundException::new);

        if (!cart.getMember().equals(member))throw new MemberNotEqualsException();

        cartItemRepository.deleteById(cartItemId);
    }

    //장바구니 물건 전체 구매
    @Transactional
    public void buyingAll(Member member){
        Cart cart = cartRepository.findCartByMember(member).orElseThrow(MemberNotEqualsException::new);
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        for (CartItem cartItem : cartItems){
            Product product = cartItem.getProduct();
            checkMemberCanBuyCartItemForEach(product,member,cartItem);

            product.setQuantity(product.getQuantity()-cartItem.getQuantity());
            member.setMoney(member.getMoney()-product.getPrice()*cartItem.getQuantity());
            product.getSeller().setMoney(product.getSeller().getMoney()+(product.getPrice()*cartItem.getQuantity()));

            History history = new History(member,product.getSeller(),product,cartItem.getQuantity());
            historyRepository.save(history);
        }
        checkMemberCanBuyCartItemAll(member);

        cartRepository.delete(cart);
    }
    public boolean checkMemberCanBuyCartItemForEach(Product product,Member member,CartItem cartItem) {
        //구매 수량 체크
        if (cartItem.getQuantity() > product.getQuantity()) throw new LakingOfProductQuantity();
        //사용자 돈 체크
        if (member.getMoney() < product.getPrice() * cartItem.getQuantity())
            throw new UserLackOfMoneyException();
        return true;
    }

    public boolean checkMemberCanBuyCartItemAll(Member member) {
        if (member.getMoney() < 0) {
            throw new UserLackOfMoneyException();
        }

        return true;
    }
}
