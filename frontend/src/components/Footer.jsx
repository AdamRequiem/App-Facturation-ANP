import React from 'react';
import { Anchor } from 'lucide-react';

export function Footer() {
  return (
    <footer className="anp-footer">
      <div className="anp-footer-container">
        <div className="anp-footer-brand">
          <div className="anp-footer-logo">
            <Anchor size={18} color="#FFFFFF" />
            <span>AGENCE NATIONALE DES PORTS</span>
          </div>
          <p>© 2026 Agence Nationale des Ports (ANP) — Royaume du Maroc. Tous droits réservés.</p>
        </div>
      </div>

      <style>{`
        .anp-footer {
          background: var(--anp-navy);
          color: rgba(255, 255, 255, 0.75);
          padding: 1.25rem 0;
          font-size: 0.825rem;
          border-top: 1px solid var(--anp-border);
          margin-top: 3rem;
        }

        .anp-footer-container {
          max-width: 1300px;
          margin: 0 auto;
          padding: 0 1rem;
          display: flex;
          align-items: center;
          justify-content: space-between;
        }

        .anp-footer-logo {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          color: #FFFFFF;
          font-weight: 700;
          margin-bottom: 0.2rem;
        }
      `}</style>
    </footer>
  );
}
