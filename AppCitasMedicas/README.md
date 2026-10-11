# App de Citas Médicas

App de consola en Java (arquitectura por capas / puertos y adaptadores).

## Cómo ejecutar
Abrir la carpeta en IntelliJ (File → Open), esperar que cargue Maven y ejecutar `app.Application`.
Los correos y SMS se **simulan** imprimiéndolos en la consola (ahí aparecen los códigos).

## Historias de usuario y verticales
| HU | Descripción | Vertical (view → service → repository) |
|----|-------------|----------------------------------------|
| HU-01 | Registro de paciente | PatientView → PatientServiceAdapter → PatientRepositoryAdapter |
| HU-02 | Inicio de sesión | AuthView → AuthServiceAdapter → SessionRepositoryAdapter |
| HU-03 | Recuperación de contraseña | RecoveryView → RecoveryServiceAdapter → RecoveryTokenRepositoryAdapter |
| HU-05 | Edición de perfil y contacto | PatientView → PatientServiceAdapter → PatientRepositoryAdapter |

Valores configurables (intentos, minutos de bloqueo, expiración de sesión, etc.): `configuration/AppSettings.java`.

## Ramas
- `main`: versión estable. Nadie hace commits directo aquí.
- `develop`: rama de integración. Aquí se unen las historias terminadas.
- `feat/hu01_registro-paciente`, `feat/hu02_inicio-sesion`, `feat/hu03_recuperar-contrasena`, `feat/hu05_editar-perfil`.

```bash
git checkout develop
git pull
git checkout -b feat/hu02_inicio-sesion
# ... trabajar y hacer commits pequeños ...
git push -u origin feat/hu02_inicio-sesion
# Abrir Pull Request en GitHub: feat/hu02_inicio-sesion -> develop
```
