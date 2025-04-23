export interface CheckResponse {
  X: number; // Координата X
  Y: number; // Координата Y
  R: number; // Радиус
  FLAG: boolean; // Флаг, указывающий, попадает ли точка в область
  OWNER: string; // Имя владельца
}
