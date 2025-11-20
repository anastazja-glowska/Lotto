package com.lotto.domain.resultchecker;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class PlayersRepositoryTestImpl implements PlayersRepository {

    Map<String, Player> playerMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Player> findById(String id) {
        return Optional.ofNullable(playerMap.get(id));
    }


    @Override
    public <S extends Player> List<S> saveAll(Iterable<S> entities) {
        Stream<S> stream = StreamSupport.stream(entities.spliterator(), false);
        List<S> list = stream.toList();
        list.forEach(player -> playerMap.put(player.hash(), player));
        return list;
    }




    @Override
    public <S extends Player> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Player> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }



    @Override
    public List<Player> findAll() {
        return List.of();
    }

    @Override
    public List<Player> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public <S extends Player> S save(S entity) {
        return null;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Player entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Player> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Player> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Player> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Player, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
