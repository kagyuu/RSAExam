#!/bin/bash

# (1) Create the secret key
openssl genrsa -out secret.key 2048

# (2) Create a public key
#     We can create public keys from a secret key easily.
openssl rsa -pubout < secret.key > public.key

# (3) Convert the secret key to PKSC8 format that Java can read.
openssl pkcs8 -in secret.key -out secret.key.pkcs8 -topk8 -nocrypt
