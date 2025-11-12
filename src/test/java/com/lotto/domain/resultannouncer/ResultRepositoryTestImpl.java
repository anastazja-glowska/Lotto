package com.lotto.domain.resultannouncer;

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

class ResultRepositoryTestImpl implements ResultRepository {


    Map<String, ResultAnswer> resultAnswerMap = new ConcurrentHashMap<>();

    @Override
    public ResultAnswer save(ResultAnswer resultAnswer) {
        resultAnswerMap.put(resultAnswer.hash(), resultAnswer);
        return resultAnswer;

    }


    @Override
    public Optional<ResultAnswer> findById(String s) {
        return Optional.ofNullable(resultAnswerMap.get(s));
    }

    @Override
    public boolean existsById(String s) {
        return resultAnswerMap.containsKey(s);
    }

    @Override
    public <S extends ResultAnswer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends ResultAnswer> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends ResultAnswer> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ResultAnswer> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ResultAnswer> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<ResultAnswer> findAll() {
        return List.of();
    }

    @Override
    public List<ResultAnswer> findAllById(Iterable<String> strings) {
        return List.of();
    }





    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(ResultAnswer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ResultAnswer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ResultAnswer> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ResultAnswer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultAnswer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ResultAnswer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultAnswer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ResultAnswer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ResultAnswer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
