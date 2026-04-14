package us.exequt.ecommerce.shared.base;

public interface BaseService<T, K> {
    T create();
    T getById(K id);
    T update(K id, T dto);
    void delete(K id);
}
