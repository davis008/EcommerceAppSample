package davis.androidapp.somco.db.model;

import java.util.UUID;

import davis.androidapp.somco.networking.model.Product;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartItem extends RealmObject {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public Product product;
    public int quantity = 0;
}
