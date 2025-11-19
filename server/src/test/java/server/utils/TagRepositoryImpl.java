package server.utils;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class TagRepositoryImpl implements TagRepository {
    public List<Tag> tags = new ArrayList<>();
    @Override
    public void flush() {

    }

    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    @Override
    public Tag getById(Long aLong) {
        return null;
    }

    @Override
    public Tag getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Tag> S save(S entity) {
        if(tags.stream().anyMatch(x -> Objects.equals(x.getId(),entity.getId()))) {
            Tag en = tags.stream().filter(x -> Objects.equals(x.getId(),entity.getId())).findAny().get();
            en.setColor(entity.getColor());
            en.setName(entity.getName());
            return (S)en;
        }
        tags.add(entity);
        return entity;
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Long aLong) {
        return tags.stream().filter(x -> Objects.equals(x.getId(), aLong)).findAny();
    }

    @Override
    public boolean existsById(Long aLong) {
        return tags.stream().anyMatch(x -> Objects.equals(x.getId(), aLong));
    }

    @Override
    public List<Tag> findAll() {
        return null;
    }

    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        tags.remove(tags.stream().filter(x -> x.getId().equals(aLong)).findAny().orElse(null));
    }

    @Override
    public void delete(Tag entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }
}
