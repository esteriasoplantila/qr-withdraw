# QR Withdraw — GitHub → Google Cloud Run

## GitHub Secrets

Set these Repository Secrets:

- `GCP_PROJECT_ID` — Google Cloud project ID
- `GCP_REGION` — e.g. `asia-southeast2`
- `GCP_WIF_PROVIDER` — full Workload Identity Federation provider resource
- `GCP_SERVICE_ACCOUNT` — deployer service account email
- `DB_URL` — PostgreSQL JDBC URL, e.g. `jdbc:postgresql://HOST:5432/postgres?sslmode=require`
- `DB_USERNAME` — PostgreSQL username
- `DB_PASSWORD` — PostgreSQL password
- `QR_EXPIRY_SECONDS` — optional, default 180

## Required Google Cloud APIs

Enable Cloud Run, Artifact Registry, IAM Credentials, and Security Token Service APIs.

The GitHub deployer service account needs permission to deploy Cloud Run, push to Artifact Registry, and act as the Cloud Run runtime service account.

## Deployment

Push to `main`. GitHub Actions will:

1. Run Maven tests.
2. Build the Docker image.
3. Push the image to Artifact Registry.
4. Deploy that image to Cloud Run.
5. Print the Cloud Run service URL.

## Database

The app is configured for PostgreSQL/Supabase. Use SSL in the JDBC URL, for example:

`jdbc:postgresql://<host>:5432/postgres?sslmode=require`

Never commit database credentials to Git.
