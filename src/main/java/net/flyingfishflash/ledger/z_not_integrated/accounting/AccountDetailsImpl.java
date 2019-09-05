package net.flyingfishflash.ledger.z_not_integrated.accounting;

import com.google.common.base.MoreObjects;
//import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an immutable account description.
 */
public final class AccountDetailsImpl implements AccountDetails {

    //@Getter
    private final String accountNumber;

    //@Getter
    private final AccountSide increaseSide;

    //@Getter
    private final String name;
    
    public String getName() {
    	return name;
    }

    public String getAccountNumber() {
    	return accountNumber;
    }
    
    public AccountSide getIncreaseSide() {
    	return increaseSide;
    }
    
    public AccountDetailsImpl(String accountNumber, String name, AccountSide increaseSide) {
        this.accountNumber = checkNotNull(accountNumber);
        this.increaseSide = checkNotNull(increaseSide);
        this.name = checkNotNull(name);
        checkArgument(!accountNumber.isEmpty());
        checkArgument(!name.isEmpty());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountNumber", accountNumber)
                .add("name", name)
                .add("increaseSide", increaseSide)
                .toString();
    }
}
