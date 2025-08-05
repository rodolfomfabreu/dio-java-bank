package dio.java.bank.repository;

import java.util.List;

import dio.java.bank.exceptions.AccountNotFoundException;
import dio.java.bank.model.AccountWallet;
import static dio.java.bank.repository.CommonsRepository.checkFundsForTransaction;

public class AccountRepository {
    private List<AccountWallet> accounts;

    public AccountWallet create(final List<String> pix, final long initialFunds) {
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "deposito");
    }

    public long withdraw(final String pix, final long amount) {
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "Pix enviado de '" + sourcePix +"' para '" + targetPix +"'";
        target.addMoney(source.reduceMoney(amount), source.getServiceType(), message);
    }

    public AccountWallet findByPix(final String pix) {
        return accounts.stream().filter(
            m -> m.getPix()
                .contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("A conta com a chave pix não foi encontrada"));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }
}
