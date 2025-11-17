package ru.siganov.hot_wallet.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.siganov.hot_wallet.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WalletRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public WalletRepositoryCustom(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deposit(Map<UUID, BigDecimal> map) {
        StringBuilder query = new StringBuilder(
                "UPDATE wallet w SET balance = w.balance + v.amount " +
                        "FROM (VALUES ");
        List<Object> params = new ArrayList<>();
        int idx = 0;
        for (Map.Entry<UUID, BigDecimal> entry : map.entrySet()) {
            params.add(entry.getKey());
            params.add(entry.getValue().toPlainString());
            query.append("(?::uuid, ?::numeric)");
            if (idx < map.size() - 1) {
                query.append(", ");
            }
            idx++;
        }
        query.append(") v(guid, amount) WHERE w.guid = v.guid");
        jdbcTemplate.update(query.toString(), params.toArray());
    }

    public void withdraw(UUID uuid, BigDecimal amount) {
        String query = """
                UPDATE wallet 
                SET balance = balance - ?::numeric  
                WHERE guid = ?::uuid
                    AND balance >= ?::numeric
                """;
        int update = jdbcTemplate.update(
                query,
                amount.toPlainString(),
                uuid,
                amount.toPlainString()
        );
        if (update == 0) {
            throw new InsufficientBalanceException();
        }
    }



}
