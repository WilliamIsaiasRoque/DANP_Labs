"""
Envia una notificacion push (Data Message) a la app lab8danp via Firebase Cloud Messaging.

USO:
    python enviar_notificacion.py                          -> usa valores por defecto
    python enviar_notificacion.py 1368337                  -> especifica el ID de pelicula
    python enviar_notificacion.py 1368337 "Titulo" "Texto" -> todo personalizado

REQUISITOS (una sola vez):
    pip install google-auth
    Tener el JSON de service account de Firebase (ver SERVICE_ACCOUNT abajo).

El token del dispositivo se detecta solo desde Logcat (necesita el emulador encendido
y la app instalada). Si no lo encuentra, lo puedes poner a mano en DEVICE_TOKEN.
"""

import json
import subprocess
import sys
import os
import urllib.request

# ---------------------------------------------------------------- CONFIGURACION
PROJECT_ID = "movies-4b18d"
SERVICE_ACCOUNT = r"C:\Users\William\Downloads\movies-4b18d-firebase-adminsdk-fbsvc-538fc50a2c.json"
ADB = r"C:\Users\William\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Si la deteccion automatica falla, pega aqui el token que sale en Logcat:
DEVICE_TOKEN = ""

CACHE = os.path.join(os.path.dirname(os.path.abspath(__file__)), ".fcm_token")
# ------------------------------------------------------------------------------


def obtener_token_dispositivo():
    """Busca el token FCM en Logcat; si no lo halla, usa el cache o el valor manual."""
    if DEVICE_TOKEN:
        return DEVICE_TOKEN

    try:
        salida = subprocess.run(
            [ADB, "logcat", "-d", "-s", "MyFirebaseService"],
            capture_output=True, text=True, timeout=30
        ).stdout
        for linea in reversed(salida.splitlines()):
            if "Nuevo token FCM:" in linea:
                token = linea.split("Nuevo token FCM:")[1].strip()
                with open(CACHE, "w") as f:
                    f.write(token)
                print("Token detectado desde Logcat.")
                return token
    except Exception as e:
        print(f"No se pudo leer Logcat ({e}).")

    if os.path.exists(CACHE):
        with open(CACHE) as f:
            token = f.read().strip()
        if token:
            print("Usando el token guardado de la vez anterior.")
            return token

    return None


def obtener_access_token():
    """Genera el access token OAuth2 a partir del service account."""
    from google.oauth2 import service_account
    import google.auth.transport.requests

    creds = service_account.Credentials.from_service_account_file(
        SERVICE_ACCOUNT, scopes=["https://www.googleapis.com/auth/firebase.messaging"]
    )
    creds.refresh(google.auth.transport.requests.Request())
    return creds.token


def enviar(movie_id, titulo, texto):
    device_token = obtener_token_dispositivo()
    if not device_token:
        print("\nERROR: no se encontro el token del dispositivo.")
        print("Abre la app en el emulador, mira Logcat con el filtro 'MyFirebaseService',")
        print("copia el token y pegalo en la variable DEVICE_TOKEN de este archivo.")
        return 1

    payload = {
        "message": {
            "token": device_token,
            "android": {"priority": "HIGH"},
            "data": {
                "movieId": str(movie_id),
                "title": titulo,
                "body": texto,
            },
        }
    }

    req = urllib.request.Request(
        f"https://fcm.googleapis.com/v1/projects/{PROJECT_ID}/messages:send",
        data=json.dumps(payload).encode("utf-8"),
        headers={
            "Authorization": f"Bearer {obtener_access_token()}",
            "Content-Type": "application/json",
        },
        method="POST",
    )

    try:
        with urllib.request.urlopen(req) as r:
            respuesta = json.load(r)
        print("\n== NOTIFICACION ENVIADA ==")
        print(f"  Pelicula: {movie_id}")
        print(f"  Titulo:   {titulo}")
        print(f"  ID Firebase: {respuesta.get('name', '?')}")
        print("\nRevisa el emulador: deberia aparecer la notificacion,")
        print("y esa pelicula sube al tope de la lista con la campana roja.")
        return 0
    except urllib.error.HTTPError as e:
        print(f"\nERROR {e.code}: {e.read().decode()}")
        print("\nSi dice UNREGISTERED, el token caduco: reinstala la app y vuelve a correr esto.")
        return 1


if __name__ == "__main__":
    movie_id = sys.argv[1] if len(sys.argv) > 1 else "1368337"
    titulo = sys.argv[2] if len(sys.argv) > 2 else "Nueva pelicula destacada"
    texto = sys.argv[3] if len(sys.argv) > 3 else "Toca para ver el detalle"
    sys.exit(enviar(movie_id, titulo, texto))
