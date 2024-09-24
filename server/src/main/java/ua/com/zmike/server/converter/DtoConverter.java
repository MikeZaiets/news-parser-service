package ua.com.zmike.server.converter;

public interface DtoConverter<T, S> {

	T convertToDto(S s);

	S convertFromDto(T t);
}
