export type Failable<R> = {
  isError: true;
  error: string;
} | {
  isError: false;
  value: R;
}

export function createError(message: string): {isError: true, error: string} {
  return {isError: true, error: message};
}

export function createValue<R>(value: R): Failable<R> {
  return {isError: false, value}
}
